import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductFormService } from './product-form.service';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
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

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let categoryService: CategoryService;
  let subCategoryService: SubCategoryService;
  let colorService: ColorService;
  let productModelService: ProductModelService;
  let sellerService: SellerService;
  let brandService: BrandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    categoryService = TestBed.inject(CategoryService);
    subCategoryService = TestBed.inject(SubCategoryService);
    colorService = TestBed.inject(ColorService);
    productModelService = TestBed.inject(ProductModelService);
    sellerService = TestBed.inject(SellerService);
    brandService = TestBed.inject(BrandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Category query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const category: ICategory = { id: 52954 };
      product.category = category;

      const categoryCollection: ICategory[] = [{ id: 63621 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining)
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SubCategory query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const subCategory: ISubCategory = { id: 84689 };
      product.subCategory = subCategory;

      const subCategoryCollection: ISubCategory[] = [{ id: 11670 }];
      jest.spyOn(subCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: subCategoryCollection })));
      const additionalSubCategories = [subCategory];
      const expectedCollection: ISubCategory[] = [...additionalSubCategories, ...subCategoryCollection];
      jest.spyOn(subCategoryService, 'addSubCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(subCategoryService.query).toHaveBeenCalled();
      expect(subCategoryService.addSubCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        subCategoryCollection,
        ...additionalSubCategories.map(expect.objectContaining)
      );
      expect(comp.subCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Color query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const color: IColor = { id: 61325 };
      product.color = color;

      const colorCollection: IColor[] = [{ id: 39471 }];
      jest.spyOn(colorService, 'query').mockReturnValue(of(new HttpResponse({ body: colorCollection })));
      const additionalColors = [color];
      const expectedCollection: IColor[] = [...additionalColors, ...colorCollection];
      jest.spyOn(colorService, 'addColorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(colorService.query).toHaveBeenCalled();
      expect(colorService.addColorToCollectionIfMissing).toHaveBeenCalledWith(
        colorCollection,
        ...additionalColors.map(expect.objectContaining)
      );
      expect(comp.colorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProductModel query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const productModel: IProductModel = { id: 72730 };
      product.productModel = productModel;

      const productModelCollection: IProductModel[] = [{ id: 58597 }];
      jest.spyOn(productModelService, 'query').mockReturnValue(of(new HttpResponse({ body: productModelCollection })));
      const additionalProductModels = [productModel];
      const expectedCollection: IProductModel[] = [...additionalProductModels, ...productModelCollection];
      jest.spyOn(productModelService, 'addProductModelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(productModelService.query).toHaveBeenCalled();
      expect(productModelService.addProductModelToCollectionIfMissing).toHaveBeenCalledWith(
        productModelCollection,
        ...additionalProductModels.map(expect.objectContaining)
      );
      expect(comp.productModelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Seller query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const seller: ISeller = { id: 27229 };
      product.seller = seller;

      const sellerCollection: ISeller[] = [{ id: 76777 }];
      jest.spyOn(sellerService, 'query').mockReturnValue(of(new HttpResponse({ body: sellerCollection })));
      const additionalSellers = [seller];
      const expectedCollection: ISeller[] = [...additionalSellers, ...sellerCollection];
      jest.spyOn(sellerService, 'addSellerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(sellerService.query).toHaveBeenCalled();
      expect(sellerService.addSellerToCollectionIfMissing).toHaveBeenCalledWith(
        sellerCollection,
        ...additionalSellers.map(expect.objectContaining)
      );
      expect(comp.sellersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Brand query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const brand: IBrand = { id: 29940 };
      product.brand = brand;

      const brandCollection: IBrand[] = [{ id: 95463 }];
      jest.spyOn(brandService, 'query').mockReturnValue(of(new HttpResponse({ body: brandCollection })));
      const additionalBrands = [brand];
      const expectedCollection: IBrand[] = [...additionalBrands, ...brandCollection];
      jest.spyOn(brandService, 'addBrandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(brandService.query).toHaveBeenCalled();
      expect(brandService.addBrandToCollectionIfMissing).toHaveBeenCalledWith(
        brandCollection,
        ...additionalBrands.map(expect.objectContaining)
      );
      expect(comp.brandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 456 };
      const category: ICategory = { id: 32126 };
      product.category = category;
      const subCategory: ISubCategory = { id: 51762 };
      product.subCategory = subCategory;
      const color: IColor = { id: 41917 };
      product.color = color;
      const productModel: IProductModel = { id: 77027 };
      product.productModel = productModel;
      const seller: ISeller = { id: 47054 };
      product.seller = seller;
      const brand: IBrand = { id: 38568 };
      product.brand = brand;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.subCategoriesSharedCollection).toContain(subCategory);
      expect(comp.colorsSharedCollection).toContain(color);
      expect(comp.productModelsSharedCollection).toContain(productModel);
      expect(comp.sellersSharedCollection).toContain(seller);
      expect(comp.brandsSharedCollection).toContain(brand);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSubCategory', () => {
      it('Should forward to subCategoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(subCategoryService, 'compareSubCategory');
        comp.compareSubCategory(entity, entity2);
        expect(subCategoryService.compareSubCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareColor', () => {
      it('Should forward to colorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(colorService, 'compareColor');
        comp.compareColor(entity, entity2);
        expect(colorService.compareColor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductModel', () => {
      it('Should forward to productModelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productModelService, 'compareProductModel');
        comp.compareProductModel(entity, entity2);
        expect(productModelService.compareProductModel).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSeller', () => {
      it('Should forward to sellerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sellerService, 'compareSeller');
        comp.compareSeller(entity, entity2);
        expect(sellerService.compareSeller).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBrand', () => {
      it('Should forward to brandService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(brandService, 'compareBrand');
        comp.compareBrand(entity, entity2);
        expect(brandService.compareBrand).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
