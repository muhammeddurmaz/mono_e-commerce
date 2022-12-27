import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductDiscountFormService } from './product-discount-form.service';
import { ProductDiscountService } from '../service/product-discount.service';
import { IProductDiscount } from '../product-discount.model';
import { IDiscount } from 'app/entities/discount/discount.model';
import { DiscountService } from 'app/entities/discount/service/discount.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ProductDiscountUpdateComponent } from './product-discount-update.component';

describe('ProductDiscount Management Update Component', () => {
  let comp: ProductDiscountUpdateComponent;
  let fixture: ComponentFixture<ProductDiscountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productDiscountFormService: ProductDiscountFormService;
  let productDiscountService: ProductDiscountService;
  let discountService: DiscountService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductDiscountUpdateComponent],
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
      .overrideTemplate(ProductDiscountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductDiscountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productDiscountFormService = TestBed.inject(ProductDiscountFormService);
    productDiscountService = TestBed.inject(ProductDiscountService);
    discountService = TestBed.inject(DiscountService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Discount query and add missing value', () => {
      const productDiscount: IProductDiscount = { id: 456 };
      const discount: IDiscount = { id: 73589 };
      productDiscount.discount = discount;

      const discountCollection: IDiscount[] = [{ id: 18193 }];
      jest.spyOn(discountService, 'query').mockReturnValue(of(new HttpResponse({ body: discountCollection })));
      const additionalDiscounts = [discount];
      const expectedCollection: IDiscount[] = [...additionalDiscounts, ...discountCollection];
      jest.spyOn(discountService, 'addDiscountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productDiscount });
      comp.ngOnInit();

      expect(discountService.query).toHaveBeenCalled();
      expect(discountService.addDiscountToCollectionIfMissing).toHaveBeenCalledWith(
        discountCollection,
        ...additionalDiscounts.map(expect.objectContaining)
      );
      expect(comp.discountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const productDiscount: IProductDiscount = { id: 456 };
      const product: IProduct = { id: 73269 };
      productDiscount.product = product;

      const productCollection: IProduct[] = [{ id: 35398 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productDiscount });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productDiscount: IProductDiscount = { id: 456 };
      const discount: IDiscount = { id: 56255 };
      productDiscount.discount = discount;
      const product: IProduct = { id: 70068 };
      productDiscount.product = product;

      activatedRoute.data = of({ productDiscount });
      comp.ngOnInit();

      expect(comp.discountsSharedCollection).toContain(discount);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.productDiscount).toEqual(productDiscount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductDiscount>>();
      const productDiscount = { id: 123 };
      jest.spyOn(productDiscountFormService, 'getProductDiscount').mockReturnValue(productDiscount);
      jest.spyOn(productDiscountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productDiscount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productDiscount }));
      saveSubject.complete();

      // THEN
      expect(productDiscountFormService.getProductDiscount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productDiscountService.update).toHaveBeenCalledWith(expect.objectContaining(productDiscount));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductDiscount>>();
      const productDiscount = { id: 123 };
      jest.spyOn(productDiscountFormService, 'getProductDiscount').mockReturnValue({ id: null });
      jest.spyOn(productDiscountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productDiscount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productDiscount }));
      saveSubject.complete();

      // THEN
      expect(productDiscountFormService.getProductDiscount).toHaveBeenCalled();
      expect(productDiscountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductDiscount>>();
      const productDiscount = { id: 123 };
      jest.spyOn(productDiscountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productDiscount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productDiscountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDiscount', () => {
      it('Should forward to discountService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(discountService, 'compareDiscount');
        comp.compareDiscount(entity, entity2);
        expect(discountService.compareDiscount).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
