import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-cart.test-samples';

import { UserCartFormService } from './user-cart-form.service';

describe('UserCart Form Service', () => {
  let service: UserCartFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserCartFormService);
  });

  describe('Service methods', () => {
    describe('createUserCartFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserCartFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cartName: expect.any(Object),
            cartNumber: expect.any(Object),
            name: expect.any(Object),
            lastName: expect.any(Object),
            sktAy: expect.any(Object),
            sktYil: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IUserCart should create a new form with FormGroup', () => {
        const formGroup = service.createUserCartFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cartName: expect.any(Object),
            cartNumber: expect.any(Object),
            name: expect.any(Object),
            lastName: expect.any(Object),
            sktAy: expect.any(Object),
            sktYil: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getUserCart', () => {
      it('should return NewUserCart for default UserCart initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserCartFormGroup(sampleWithNewData);

        const userCart = service.getUserCart(formGroup) as any;

        expect(userCart).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserCart for empty UserCart initial value', () => {
        const formGroup = service.createUserCartFormGroup();

        const userCart = service.getUserCart(formGroup) as any;

        expect(userCart).toMatchObject({});
      });

      it('should return IUserCart', () => {
        const formGroup = service.createUserCartFormGroup(sampleWithRequiredData);

        const userCart = service.getUserCart(formGroup) as any;

        expect(userCart).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserCart should not enable id FormControl', () => {
        const formGroup = service.createUserCartFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserCart should disable id FormControl', () => {
        const formGroup = service.createUserCartFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
