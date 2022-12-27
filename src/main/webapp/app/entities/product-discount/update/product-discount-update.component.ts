import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductDiscountFormService, ProductDiscountFormGroup } from './product-discount-form.service';
import { IProductDiscount } from '../product-discount.model';
import { ProductDiscountService } from '../service/product-discount.service';
import { IDiscount } from 'app/entities/discount/discount.model';
import { DiscountService } from 'app/entities/discount/service/discount.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-product-discount-update',
  templateUrl: './product-discount-update.component.html',
})
export class ProductDiscountUpdateComponent implements OnInit {
  isSaving = false;
  productDiscount: IProductDiscount | null = null;

  discountsSharedCollection: IDiscount[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: ProductDiscountFormGroup = this.productDiscountFormService.createProductDiscountFormGroup();

  constructor(
    protected productDiscountService: ProductDiscountService,
    protected productDiscountFormService: ProductDiscountFormService,
    protected discountService: DiscountService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDiscount = (o1: IDiscount | null, o2: IDiscount | null): boolean => this.discountService.compareDiscount(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productDiscount }) => {
      this.productDiscount = productDiscount;
      if (productDiscount) {
        this.updateForm(productDiscount);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productDiscount = this.productDiscountFormService.getProductDiscount(this.editForm);
    if (productDiscount.id !== null) {
      this.subscribeToSaveResponse(this.productDiscountService.update(productDiscount));
    } else {
      this.subscribeToSaveResponse(this.productDiscountService.create(productDiscount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductDiscount>>): void {
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

  protected updateForm(productDiscount: IProductDiscount): void {
    this.productDiscount = productDiscount;
    this.productDiscountFormService.resetForm(this.editForm, productDiscount);

    this.discountsSharedCollection = this.discountService.addDiscountToCollectionIfMissing<IDiscount>(
      this.discountsSharedCollection,
      productDiscount.discount
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      productDiscount.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.discountService
      .query()
      .pipe(map((res: HttpResponse<IDiscount[]>) => res.body ?? []))
      .pipe(
        map((discounts: IDiscount[]) =>
          this.discountService.addDiscountToCollectionIfMissing<IDiscount>(discounts, this.productDiscount?.discount)
        )
      )
      .subscribe((discounts: IDiscount[]) => (this.discountsSharedCollection = discounts));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productDiscount?.product)
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
