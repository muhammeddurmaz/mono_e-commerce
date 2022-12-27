import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserAdressFormService } from './user-adress-form.service';
import { UserAdressService } from '../service/user-adress.service';
import { IUserAdress } from '../user-adress.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserAdressUpdateComponent } from './user-adress-update.component';

describe('UserAdress Management Update Component', () => {
  let comp: UserAdressUpdateComponent;
  let fixture: ComponentFixture<UserAdressUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAdressFormService: UserAdressFormService;
  let userAdressService: UserAdressService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserAdressUpdateComponent],
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
      .overrideTemplate(UserAdressUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAdressUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAdressFormService = TestBed.inject(UserAdressFormService);
    userAdressService = TestBed.inject(UserAdressService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userAdress: IUserAdress = { id: 456 };
      const user: IUser = { id: 72548 };
      userAdress.user = user;

      const userCollection: IUser[] = [{ id: 5033 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAdress });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userAdress: IUserAdress = { id: 456 };
      const user: IUser = { id: 33637 };
      userAdress.user = user;

      activatedRoute.data = of({ userAdress });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.userAdress).toEqual(userAdress);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAdress>>();
      const userAdress = { id: 123 };
      jest.spyOn(userAdressFormService, 'getUserAdress').mockReturnValue(userAdress);
      jest.spyOn(userAdressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAdress });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAdress }));
      saveSubject.complete();

      // THEN
      expect(userAdressFormService.getUserAdress).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAdressService.update).toHaveBeenCalledWith(expect.objectContaining(userAdress));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAdress>>();
      const userAdress = { id: 123 };
      jest.spyOn(userAdressFormService, 'getUserAdress').mockReturnValue({ id: null });
      jest.spyOn(userAdressService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAdress: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAdress }));
      saveSubject.complete();

      // THEN
      expect(userAdressFormService.getUserAdress).toHaveBeenCalled();
      expect(userAdressService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAdress>>();
      const userAdress = { id: 123 };
      jest.spyOn(userAdressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAdress });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAdressService.update).toHaveBeenCalled();
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
