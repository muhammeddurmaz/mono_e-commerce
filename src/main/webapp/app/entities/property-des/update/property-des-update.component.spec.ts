import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PropertyDesFormService } from './property-des-form.service';
import { PropertyDesService } from '../service/property-des.service';
import { IPropertyDes } from '../property-des.model';
import { IProperty } from 'app/entities/property/property.model';
import { PropertyService } from 'app/entities/property/service/property.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { PropertyDesUpdateComponent } from './property-des-update.component';

describe('PropertyDes Management Update Component', () => {
  let comp: PropertyDesUpdateComponent;
  let fixture: ComponentFixture<PropertyDesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let propertyDesFormService: PropertyDesFormService;
  let propertyDesService: PropertyDesService;
  let propertyService: PropertyService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PropertyDesUpdateComponent],
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
      .overrideTemplate(PropertyDesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PropertyDesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    propertyDesFormService = TestBed.inject(PropertyDesFormService);
    propertyDesService = TestBed.inject(PropertyDesService);
    propertyService = TestBed.inject(PropertyService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Property query and add missing value', () => {
      const propertyDes: IPropertyDes = { id: 456 };
      const property: IProperty = { id: 31789 };
      propertyDes.property = property;

      const propertyCollection: IProperty[] = [{ id: 36250 }];
      jest.spyOn(propertyService, 'query').mockReturnValue(of(new HttpResponse({ body: propertyCollection })));
      const additionalProperties = [property];
      const expectedCollection: IProperty[] = [...additionalProperties, ...propertyCollection];
      jest.spyOn(propertyService, 'addPropertyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ propertyDes });
      comp.ngOnInit();

      expect(propertyService.query).toHaveBeenCalled();
      expect(propertyService.addPropertyToCollectionIfMissing).toHaveBeenCalledWith(
        propertyCollection,
        ...additionalProperties.map(expect.objectContaining)
      );
      expect(comp.propertiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const propertyDes: IPropertyDes = { id: 456 };
      const product: IProduct = { id: 42239 };
      propertyDes.product = product;

      const productCollection: IProduct[] = [{ id: 37304 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ propertyDes });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const propertyDes: IPropertyDes = { id: 456 };
      const property: IProperty = { id: 8415 };
      propertyDes.property = property;
      const product: IProduct = { id: 58912 };
      propertyDes.product = product;

      activatedRoute.data = of({ propertyDes });
      comp.ngOnInit();

      expect(comp.propertiesSharedCollection).toContain(property);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.propertyDes).toEqual(propertyDes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPropertyDes>>();
      const propertyDes = { id: 123 };
      jest.spyOn(propertyDesFormService, 'getPropertyDes').mockReturnValue(propertyDes);
      jest.spyOn(propertyDesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ propertyDes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: propertyDes }));
      saveSubject.complete();

      // THEN
      expect(propertyDesFormService.getPropertyDes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(propertyDesService.update).toHaveBeenCalledWith(expect.objectContaining(propertyDes));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPropertyDes>>();
      const propertyDes = { id: 123 };
      jest.spyOn(propertyDesFormService, 'getPropertyDes').mockReturnValue({ id: null });
      jest.spyOn(propertyDesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ propertyDes: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: propertyDes }));
      saveSubject.complete();

      // THEN
      expect(propertyDesFormService.getPropertyDes).toHaveBeenCalled();
      expect(propertyDesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPropertyDes>>();
      const propertyDes = { id: 123 };
      jest.spyOn(propertyDesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ propertyDes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(propertyDesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProperty', () => {
      it('Should forward to propertyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(propertyService, 'compareProperty');
        comp.compareProperty(entity, entity2);
        expect(propertyService.compareProperty).toHaveBeenCalledWith(entity, entity2);
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
