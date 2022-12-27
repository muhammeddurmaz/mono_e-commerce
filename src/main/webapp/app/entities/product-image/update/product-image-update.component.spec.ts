import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductImageFormService } from './product-image-form.service';
import { ProductImageService } from '../service/product-image.service';
import { IProductImage } from '../product-image.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ProductImageUpdateComponent } from './product-image-update.component';

describe('ProductImage Management Update Component', () => {
  let comp: ProductImageUpdateComponent;
  let fixture: ComponentFixture<ProductImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productImageFormService: ProductImageFormService;
  let productImageService: ProductImageService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductImageUpdateComponent],
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
      .overrideTemplate(ProductImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productImageFormService = TestBed.inject(ProductImageFormService);
    productImageService = TestBed.inject(ProductImageService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const productImage: IProductImage = { id: 456 };
      const product: IProduct = { id: 63146 };
      productImage.product = product;

      const productCollection: IProduct[] = [{ id: 75295 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productImage: IProductImage = { id: 456 };
      const product: IProduct = { id: 48339 };
      productImage.product = product;

      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.productImage).toEqual(productImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageFormService, 'getProductImage').mockReturnValue(productImage);
      jest.spyOn(productImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productImage }));
      saveSubject.complete();

      // THEN
      expect(productImageFormService.getProductImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productImageService.update).toHaveBeenCalledWith(expect.objectContaining(productImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageFormService, 'getProductImage').mockReturnValue({ id: null });
      jest.spyOn(productImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productImage }));
      saveSubject.complete();

      // THEN
      expect(productImageFormService.getProductImage).toHaveBeenCalled();
      expect(productImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
