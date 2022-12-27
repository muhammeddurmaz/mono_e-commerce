import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductInventoryFormService } from './product-inventory-form.service';
import { ProductInventoryService } from '../service/product-inventory.service';
import { IProductInventory } from '../product-inventory.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ProductInventoryUpdateComponent } from './product-inventory-update.component';

describe('ProductInventory Management Update Component', () => {
  let comp: ProductInventoryUpdateComponent;
  let fixture: ComponentFixture<ProductInventoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productInventoryFormService: ProductInventoryFormService;
  let productInventoryService: ProductInventoryService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductInventoryUpdateComponent],
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
      .overrideTemplate(ProductInventoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductInventoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productInventoryFormService = TestBed.inject(ProductInventoryFormService);
    productInventoryService = TestBed.inject(ProductInventoryService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const productInventory: IProductInventory = { id: 456 };
      const product: IProduct = { id: 25300 };
      productInventory.product = product;

      const productCollection: IProduct[] = [{ id: 9524 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productInventory: IProductInventory = { id: 456 };
      const product: IProduct = { id: 5809 };
      productInventory.product = product;

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.productInventory).toEqual(productInventory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductInventory>>();
      const productInventory = { id: 123 };
      jest.spyOn(productInventoryFormService, 'getProductInventory').mockReturnValue(productInventory);
      jest.spyOn(productInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productInventory }));
      saveSubject.complete();

      // THEN
      expect(productInventoryFormService.getProductInventory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productInventoryService.update).toHaveBeenCalledWith(expect.objectContaining(productInventory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductInventory>>();
      const productInventory = { id: 123 };
      jest.spyOn(productInventoryFormService, 'getProductInventory').mockReturnValue({ id: null });
      jest.spyOn(productInventoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInventory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productInventory }));
      saveSubject.complete();

      // THEN
      expect(productInventoryFormService.getProductInventory).toHaveBeenCalled();
      expect(productInventoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductInventory>>();
      const productInventory = { id: 123 };
      jest.spyOn(productInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productInventoryService.update).toHaveBeenCalled();
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
