import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProductTypeFormService, ProductTypeFormGroup } from './product-type-form.service';
import { IProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

@Component({
  selector: 'jhi-product-type-update',
  templateUrl: './product-type-update.component.html',
})
export class ProductTypeUpdateComponent implements OnInit {
  isSaving = false;
  productType: IProductType | null = null;

  editForm: ProductTypeFormGroup = this.productTypeFormService.createProductTypeFormGroup();

  constructor(
    protected productTypeService: ProductTypeService,
    protected productTypeFormService: ProductTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productType }) => {
      this.productType = productType;
      if (productType) {
        this.updateForm(productType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productType = this.productTypeFormService.getProductType(this.editForm);
    if (productType.id !== null) {
      this.subscribeToSaveResponse(this.productTypeService.update(productType));
    } else {
      this.subscribeToSaveResponse(this.productTypeService.create(productType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductType>>): void {
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

  protected updateForm(productType: IProductType): void {
    this.productType = productType;
    this.productTypeFormService.resetForm(this.editForm, productType);
  }
}
