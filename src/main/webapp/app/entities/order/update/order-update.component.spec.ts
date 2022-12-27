import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderFormService } from './order-form.service';
import { OrderService } from '../service/order.service';
import { IOrder } from '../order.model';
import { IUserAdress } from 'app/entities/user-adress/user-adress.model';
import { UserAdressService } from 'app/entities/user-adress/service/user-adress.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Order Management Update Component', () => {
  let comp: OrderUpdateComponent;
  let fixture: ComponentFixture<OrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderFormService: OrderFormService;
  let orderService: OrderService;
  let userAdressService: UserAdressService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderUpdateComponent],
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
      .overrideTemplate(OrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderFormService = TestBed.inject(OrderFormService);
    orderService = TestBed.inject(OrderService);
    userAdressService = TestBed.inject(UserAdressService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserAdress query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const adress: IUserAdress = { id: 75238 };
      order.adress = adress;

      const userAdressCollection: IUserAdress[] = [{ id: 5529 }];
      jest.spyOn(userAdressService, 'query').mockReturnValue(of(new HttpResponse({ body: userAdressCollection })));
      const additionalUserAdresses = [adress];
      const expectedCollection: IUserAdress[] = [...additionalUserAdresses, ...userAdressCollection];
      jest.spyOn(userAdressService, 'addUserAdressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(userAdressService.query).toHaveBeenCalled();
      expect(userAdressService.addUserAdressToCollectionIfMissing).toHaveBeenCalledWith(
        userAdressCollection,
        ...additionalUserAdresses.map(expect.objectContaining)
      );
      expect(comp.userAdressesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const user: IUser = { id: 95442 };
      order.user = user;

      const userCollection: IUser[] = [{ id: 55391 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const order: IOrder = { id: 456 };
      const adress: IUserAdress = { id: 17189 };
      order.adress = adress;
      const user: IUser = { id: 60405 };
      order.user = user;

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(comp.userAdressesSharedCollection).toContain(adress);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.order).toEqual(order);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue(order);
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderService.update).toHaveBeenCalledWith(expect.objectContaining(order));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue({ id: null });
      jest.spyOn(orderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(orderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserAdress', () => {
      it('Should forward to userAdressService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userAdressService, 'compareUserAdress');
        comp.compareUserAdress(entity, entity2);
        expect(userAdressService.compareUserAdress).toHaveBeenCalledWith(entity, entity2);
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
