import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BrandFormService } from './brand-form.service';
import { BrandService } from '../service/brand.service';
import { IBrand } from '../brand.model';
import { ISeller } from 'app/entities/seller/seller.model';
import { SellerService } from 'app/entities/seller/service/seller.service';

import { BrandUpdateComponent } from './brand-update.component';

describe('Brand Management Update Component', () => {
  let comp: BrandUpdateComponent;
  let fixture: ComponentFixture<BrandUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let brandFormService: BrandFormService;
  let brandService: BrandService;
  let sellerService: SellerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BrandUpdateComponent],
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
      .overrideTemplate(BrandUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BrandUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    brandFormService = TestBed.inject(BrandFormService);
    brandService = TestBed.inject(BrandService);
    sellerService = TestBed.inject(SellerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call seller query and add missing value', () => {
      const brand: IBrand = { id: 456 };
      const seller: ISeller = { id: 66917 };
      brand.seller = seller;

      const sellerCollection: ISeller[] = [{ id: 23754 }];
      jest.spyOn(sellerService, 'query').mockReturnValue(of(new HttpResponse({ body: sellerCollection })));
      const expectedCollection: ISeller[] = [seller, ...sellerCollection];
      jest.spyOn(sellerService, 'addSellerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ brand });
      comp.ngOnInit();

      expect(sellerService.query).toHaveBeenCalled();
      expect(sellerService.addSellerToCollectionIfMissing).toHaveBeenCalledWith(sellerCollection, seller);
      expect(comp.sellersCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const brand: IBrand = { id: 456 };
      const seller: ISeller = { id: 55559 };
      brand.seller = seller;

      activatedRoute.data = of({ brand });
      comp.ngOnInit();

      expect(comp.sellersCollection).toContain(seller);
      expect(comp.brand).toEqual(brand);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBrand>>();
      const brand = { id: 123 };
      jest.spyOn(brandFormService, 'getBrand').mockReturnValue(brand);
      jest.spyOn(brandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ brand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: brand }));
      saveSubject.complete();

      // THEN
      expect(brandFormService.getBrand).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(brandService.update).toHaveBeenCalledWith(expect.objectContaining(brand));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBrand>>();
      const brand = { id: 123 };
      jest.spyOn(brandFormService, 'getBrand').mockReturnValue({ id: null });
      jest.spyOn(brandService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ brand: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: brand }));
      saveSubject.complete();

      // THEN
      expect(brandFormService.getBrand).toHaveBeenCalled();
      expect(brandService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBrand>>();
      const brand = { id: 123 };
      jest.spyOn(brandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ brand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(brandService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSeller', () => {
      it('Should forward to sellerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sellerService, 'compareSeller');
        comp.compareSeller(entity, entity2);
        expect(sellerService.compareSeller).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
