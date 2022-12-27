import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SellerFormService } from './seller-form.service';
import { SellerService } from '../service/seller.service';
import { ISeller } from '../seller.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProductType } from 'app/entities/product-type/product-type.model';
import { ProductTypeService } from 'app/entities/product-type/service/product-type.service';

import { SellerUpdateComponent } from './seller-update.component';

describe('Seller Management Update Component', () => {
  let comp: SellerUpdateComponent;
  let fixture: ComponentFixture<SellerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sellerFormService: SellerFormService;
  let sellerService: SellerService;
  let userService: UserService;
  let productTypeService: ProductTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SellerUpdateComponent],
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
      .overrideTemplate(SellerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SellerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sellerFormService = TestBed.inject(SellerFormService);
    sellerService = TestBed.inject(SellerService);
    userService = TestBed.inject(UserService);
    productTypeService = TestBed.inject(ProductTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const seller: ISeller = { id: 456 };
      const user: IUser = { id: 92718 };
      seller.user = user;

      const userCollection: IUser[] = [{ id: 39627 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProductType query and add missing value', () => {
      const seller: ISeller = { id: 456 };
      const sellerProductType: IProductType = { id: 58158 };
      seller.sellerProductType = sellerProductType;

      const productTypeCollection: IProductType[] = [{ id: 32314 }];
      jest.spyOn(productTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: productTypeCollection })));
      const additionalProductTypes = [sellerProductType];
      const expectedCollection: IProductType[] = [...additionalProductTypes, ...productTypeCollection];
      jest.spyOn(productTypeService, 'addProductTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      expect(productTypeService.query).toHaveBeenCalled();
      expect(productTypeService.addProductTypeToCollectionIfMissing).toHaveBeenCalledWith(
        productTypeCollection,
        ...additionalProductTypes.map(expect.objectContaining)
      );
      expect(comp.productTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const seller: ISeller = { id: 456 };
      const user: IUser = { id: 34417 };
      seller.user = user;
      const sellerProductType: IProductType = { id: 81441 };
      seller.sellerProductType = sellerProductType;

      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.productTypesSharedCollection).toContain(sellerProductType);
      expect(comp.seller).toEqual(seller);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeller>>();
      const seller = { id: 123 };
      jest.spyOn(sellerFormService, 'getSeller').mockReturnValue(seller);
      jest.spyOn(sellerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seller }));
      saveSubject.complete();

      // THEN
      expect(sellerFormService.getSeller).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sellerService.update).toHaveBeenCalledWith(expect.objectContaining(seller));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeller>>();
      const seller = { id: 123 };
      jest.spyOn(sellerFormService, 'getSeller').mockReturnValue({ id: null });
      jest.spyOn(sellerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seller: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seller }));
      saveSubject.complete();

      // THEN
      expect(sellerFormService.getSeller).toHaveBeenCalled();
      expect(sellerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeller>>();
      const seller = { id: 123 };
      jest.spyOn(sellerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sellerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductType', () => {
      it('Should forward to productTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productTypeService, 'compareProductType');
        comp.compareProductType(entity, entity2);
        expect(productTypeService.compareProductType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
