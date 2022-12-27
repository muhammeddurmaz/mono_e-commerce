import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductModelFormService } from './product-model-form.service';
import { ProductModelService } from '../service/product-model.service';
import { IProductModel } from '../product-model.model';

import { ProductModelUpdateComponent } from './product-model-update.component';

describe('ProductModel Management Update Component', () => {
  let comp: ProductModelUpdateComponent;
  let fixture: ComponentFixture<ProductModelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productModelFormService: ProductModelFormService;
  let productModelService: ProductModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductModelUpdateComponent],
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
      .overrideTemplate(ProductModelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductModelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productModelFormService = TestBed.inject(ProductModelFormService);
    productModelService = TestBed.inject(ProductModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const productModel: IProductModel = { id: 456 };

      activatedRoute.data = of({ productModel });
      comp.ngOnInit();

      expect(comp.productModel).toEqual(productModel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductModel>>();
      const productModel = { id: 123 };
      jest.spyOn(productModelFormService, 'getProductModel').mockReturnValue(productModel);
      jest.spyOn(productModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productModel }));
      saveSubject.complete();

      // THEN
      expect(productModelFormService.getProductModel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productModelService.update).toHaveBeenCalledWith(expect.objectContaining(productModel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductModel>>();
      const productModel = { id: 123 };
      jest.spyOn(productModelFormService, 'getProductModel').mockReturnValue({ id: null });
      jest.spyOn(productModelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productModel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productModel }));
      saveSubject.complete();

      // THEN
      expect(productModelFormService.getProductModel).toHaveBeenCalled();
      expect(productModelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductModel>>();
      const productModel = { id: 123 };
      jest.spyOn(productModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productModelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
