import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserCartFormService } from './user-cart-form.service';
import { UserCartService } from '../service/user-cart.service';
import { IUserCart } from '../user-cart.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserCartUpdateComponent } from './user-cart-update.component';

describe('UserCart Management Update Component', () => {
  let comp: UserCartUpdateComponent;
  let fixture: ComponentFixture<UserCartUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userCartFormService: UserCartFormService;
  let userCartService: UserCartService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserCartUpdateComponent],
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
      .overrideTemplate(UserCartUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserCartUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userCartFormService = TestBed.inject(UserCartFormService);
    userCartService = TestBed.inject(UserCartService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userCart: IUserCart = { id: 456 };
      const user: IUser = { id: 43193 };
      userCart.user = user;

      const userCollection: IUser[] = [{ id: 10673 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userCart });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userCart: IUserCart = { id: 456 };
      const user: IUser = { id: 36884 };
      userCart.user = user;

      activatedRoute.data = of({ userCart });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.userCart).toEqual(userCart);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserCart>>();
      const userCart = { id: 123 };
      jest.spyOn(userCartFormService, 'getUserCart').mockReturnValue(userCart);
      jest.spyOn(userCartService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userCart });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userCart }));
      saveSubject.complete();

      // THEN
      expect(userCartFormService.getUserCart).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userCartService.update).toHaveBeenCalledWith(expect.objectContaining(userCart));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserCart>>();
      const userCart = { id: 123 };
      jest.spyOn(userCartFormService, 'getUserCart').mockReturnValue({ id: null });
      jest.spyOn(userCartService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userCart: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userCart }));
      saveSubject.complete();

      // THEN
      expect(userCartFormService.getUserCart).toHaveBeenCalled();
      expect(userCartService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserCart>>();
      const userCart = { id: 123 };
      jest.spyOn(userCartService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userCart });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userCartService.update).toHaveBeenCalled();
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
  });
});
