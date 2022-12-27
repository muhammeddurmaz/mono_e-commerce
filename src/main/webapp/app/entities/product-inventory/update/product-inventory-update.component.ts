import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductInventoryFormService, ProductInventoryFormGroup } from './product-inventory-form.service';
import { IProductInventory } from '../product-inventory.model';
import { ProductInventoryService } from '../service/product-inventory.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-product-inventory-update',
  templateUrl: './product-inventory-update.component.html',
})
export class ProductInventoryUpdateComponent implements OnInit {
  isSaving = false;
  productInventory: IProductInventory | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: ProductInventoryFormGroup = this.productInventoryFormService.createProductInventoryFormGroup();

  constructor(
    protected productInventoryService: ProductInventoryService,
    protected productInventoryFormService: ProductInventoryFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInventory }) => {
      this.productInventory = productInventory;
      if (productInventory) {
        this.updateForm(productInventory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productInventory = this.productInventoryFormService.getProductInventory(this.editForm);
    if (productInventory.id !== null) {
      this.subscribeToSaveResponse(this.productInventoryService.update(productInventory));
    } else {
      this.subscribeToSaveResponse(this.productInventoryService.create(productInventory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductInventory>>): void {
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

  protected updateForm(productInventory: IProductInventory): void {
    this.productInventory = productInventory;
    this.productInventoryFormService.resetForm(this.editForm, productInventory);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      productInventory.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productInventory?.product)
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
