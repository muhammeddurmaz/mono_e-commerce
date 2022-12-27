import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FavoriteFormService } from './favorite-form.service';
import { FavoriteService } from '../service/favorite.service';
import { IFavorite } from '../favorite.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { FavoriteUpdateComponent } from './favorite-update.component';

describe('Favorite Management Update Component', () => {
  let comp: FavoriteUpdateComponent;
  let fixture: ComponentFixture<FavoriteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let favoriteFormService: FavoriteFormService;
  let favoriteService: FavoriteService;
  let productService: ProductService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FavoriteUpdateComponent],
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
      .overrideTemplate(FavoriteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FavoriteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    favoriteFormService = TestBed.inject(FavoriteFormService);
    favoriteService = TestBed.inject(FavoriteService);
    productService = TestBed.inject(ProductService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const favorite: IFavorite = { id: 456 };
      const product: IProduct = { id: 1268 };
      favorite.product = product;

      const productCollection: IProduct[] = [{ id: 76226 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ favorite });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const favorite: IFavorite = { id: 456 };
      const user: IUser = { id: 70170 };
      favorite.user = user;

      const userCollection: IUser[] = [{ id: 96014 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ favorite });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const favorite: IFavorite = { id: 456 };
      const product: IProduct = { id: 90123 };
      favorite.product = product;
      const user: IUser = { id: 28489 };
      favorite.user = user;

      activatedRoute.data = of({ favorite });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.favorite).toEqual(favorite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFavorite>>();
      const favorite = { id: 123 };
      jest.spyOn(favoriteFormService, 'getFavorite').mockReturnValue(favorite);
      jest.spyOn(favoriteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favorite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favorite }));
      saveSubject.complete();

      // THEN
      expect(favoriteFormService.getFavorite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(favoriteService.update).toHaveBeenCalledWith(expect.objectContaining(favorite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFavorite>>();
      const favorite = { id: 123 };
      jest.spyOn(favoriteFormService, 'getFavorite').mockReturnValue({ id: null });
      jest.spyOn(favoriteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favorite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favorite }));
      saveSubject.complete();

      // THEN
      expect(favoriteFormService.getFavorite).toHaveBeenCalled();
      expect(favoriteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFavorite>>();
      const favorite = { id: 123 };
      jest.spyOn(favoriteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favorite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(favoriteService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
