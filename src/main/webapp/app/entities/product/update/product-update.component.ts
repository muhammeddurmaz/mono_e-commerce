import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductFormService, ProductFormGroup } from './product-form.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { IColor } from 'app/entities/color/color.model';
import { ColorService } from 'app/entities/color/service/color.service';
import { IProductModel } from 'app/entities/product-model/product-model.model';
import { ProductModelService } from 'app/entities/product-model/service/product-model.service';
import { ISeller } from 'app/entities/seller/seller.model';
import { SellerService } from 'app/entities/seller/service/seller.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;

  categoriesSharedCollection: ICategory[] = [];
  subCategoriesSharedCollection: ISubCategory[] = [];
  colorsSharedCollection: IColor[] = [];
  productModelsSharedCollection: IProductModel[] = [];
  sellersSharedCollection: ISeller[] = [];
  brandsSharedCollection: IBrand[] = [];

  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productService: ProductService,
    protected productFormService: ProductFormService,
    protected categoryService: CategoryService,
    protected subCategoryService: SubCategoryService,
    protected colorService: ColorService,
    protected productModelService: ProductModelService,
    protected sellerService: SellerService,
    protected brandService: BrandService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareSubCategory = (o1: ISubCategory | null, o2: ISubCategory | null): boolean => this.subCategoryService.compareSubCategory(o1, o2);

  compareColor = (o1: IColor | null, o2: IColor | null): boolean => this.colorService.compareColor(o1, o2);

  compareProductModel = (o1: IProductModel | null, o2: IProductModel | null): boolean =>
    this.productModelService.compareProductModel(o1, o2);

  compareSeller = (o1: ISeller | null, o2: ISeller | null): boolean => this.sellerService.compareSeller(o1, o2);

  compareBrand = (o1: IBrand | null, o2: IBrand | null): boolean => this.brandService.compareBrand(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('fastShopApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      product.category
    );
    this.subCategoriesSharedCollection = this.subCategoryService.addSubCategoryToCollectionIfMissing<ISubCategory>(
      this.subCategoriesSharedCollection,
      product.subCategory
    );
    this.colorsSharedCollection = this.colorService.addColorToCollectionIfMissing<IColor>(this.colorsSharedCollection, product.color);
    this.productModelsSharedCollection = this.productModelService.addProductModelToCollectionIfMissing<IProductModel>(
      this.productModelsSharedCollection,
      product.productModel
    );
    this.sellersSharedCollection = this.sellerService.addSellerToCollectionIfMissing<ISeller>(this.sellersSharedCollection, product.seller);
    this.brandsSharedCollection = this.brandService.addBrandToCollectionIfMissing<IBrand>(this.brandsSharedCollection, product.brand);
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.product?.category)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.subCategoryService
      .query()
      .pipe(map((res: HttpResponse<ISubCategory[]>) => res.body ?? []))
      .pipe(
        map((subCategories: ISubCategory[]) =>
          this.subCategoryService.addSubCategoryToCollectionIfMissing<ISubCategory>(subCategories, this.product?.subCategory)
        )
      )
      .subscribe((subCategories: ISubCategory[]) => (this.subCategoriesSharedCollection = subCategories));

    this.colorService
      .query()
      .pipe(map((res: HttpResponse<IColor[]>) => res.body ?? []))
      .pipe(map((colors: IColor[]) => this.colorService.addColorToCollectionIfMissing<IColor>(colors, this.product?.color)))
      .subscribe((colors: IColor[]) => (this.colorsSharedCollection = colors));

    this.productModelService
      .query()
      .pipe(map((res: HttpResponse<IProductModel[]>) => res.body ?? []))
      .pipe(
        map((productModels: IProductModel[]) =>
          this.productModelService.addProductModelToCollectionIfMissing<IProductModel>(productModels, this.product?.productModel)
        )
      )
      .subscribe((productModels: IProductModel[]) => (this.productModelsSharedCollection = productModels));

    this.sellerService
      .query()
      .pipe(map((res: HttpResponse<ISeller[]>) => res.body ?? []))
      .pipe(map((sellers: ISeller[]) => this.sellerService.addSellerToCollectionIfMissing<ISeller>(sellers, this.product?.seller)))
      .subscribe((sellers: ISeller[]) => (this.sellersSharedCollection = sellers));

    this.brandService
      .query()
      .pipe(map((res: HttpResponse<IBrand[]>) => res.body ?? []))
      .pipe(map((brands: IBrand[]) => this.brandService.addBrandToCollectionIfMissing<IBrand>(brands, this.product?.brand)))
      .subscribe((brands: IBrand[]) => (this.brandsSharedCollection = brands));
  }
}
