import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PropertyDesFormService, PropertyDesFormGroup } from './property-des-form.service';
import { IPropertyDes } from '../property-des.model';
import { PropertyDesService } from '../service/property-des.service';
import { IProperty } from 'app/entities/property/property.model';
import { PropertyService } from 'app/entities/property/service/property.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-property-des-update',
  templateUrl: './property-des-update.component.html',
})
export class PropertyDesUpdateComponent implements OnInit {
  isSaving = false;
  propertyDes: IPropertyDes | null = null;

  propertiesSharedCollection: IProperty[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: PropertyDesFormGroup = this.propertyDesFormService.createPropertyDesFormGroup();

  constructor(
    protected propertyDesService: PropertyDesService,
    protected propertyDesFormService: PropertyDesFormService,
    protected propertyService: PropertyService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProperty = (o1: IProperty | null, o2: IProperty | null): boolean => this.propertyService.compareProperty(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyDes }) => {
      this.propertyDes = propertyDes;
      if (propertyDes) {
        this.updateForm(propertyDes);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const propertyDes = this.propertyDesFormService.getPropertyDes(this.editForm);
    if (propertyDes.id !== null) {
      this.subscribeToSaveResponse(this.propertyDesService.update(propertyDes));
    } else {
      this.subscribeToSaveResponse(this.propertyDesService.create(propertyDes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPropertyDes>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(propertyDes: IPropertyDes): void {
    this.propertyDes = propertyDes;
    this.propertyDesFormService.resetForm(this.editForm, propertyDes);

    this.propertiesSharedCollection = this.propertyService.addPropertyToCollectionIfMissing<IProperty>(
      this.propertiesSharedCollection,
      propertyDes.property
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      propertyDes.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.propertyService
      .query()
      .pipe(map((res: HttpResponse<IProperty[]>) => res.body ?? []))
      .pipe(
        map((properties: IProperty[]) =>
          this.propertyService.addPropertyToCollectionIfMissing<IProperty>(properties, this.propertyDes?.property)
        )
      )
      .subscribe((properties: IProperty[]) => (this.propertiesSharedCollection = properties));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.propertyDes?.product))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
