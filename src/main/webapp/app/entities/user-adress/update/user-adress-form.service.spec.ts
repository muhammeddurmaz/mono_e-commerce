import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-adress.test-samples';

import { UserAdressFormService } from './user-adress-form.service';

describe('UserAdress Form Service', () => {
  let service: UserAdressFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserAdressFormService);
  });

  describe('Service methods', () => {
    describe('createUserAdressFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserAdressFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            lastName: expect.any(Object),
            telephone: expect.any(Object),
            city: expect.any(Object),
            adress: expect.any(Object),
            adressTitle: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IUserAdress should create a new form with FormGroup', () => {
        const formGroup = service.createUserAdressFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            lastName: expect.any(Object),
            telephone: expect.any(Object),
            city: expect.any(Object),
            adress: expect.any(Object),
            adressTitle: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getUserAdress', () => {
      it('should return NewUserAdress for default UserAdress initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserAdressFormGroup(sampleWithNewData);

        const userAdress = service.getUserAdress(formGroup) as any;

        expect(userAdress).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserAdress for empty UserAdress initial value', () => {
        const formGroup = service.createUserAdressFormGroup();

        const userAdress = service.getUserAdress(formGroup) as any;

        expect(userAdress).toMatchObject({});
      });

      it('should return IUserAdress', () => {
        const formGroup = service.createUserAdressFormGroup(sampleWithRequiredData);

        const userAdress = service.getUserAdress(formGroup) as any;

        expect(userAdress).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserAdress should not enable id FormControl', () => {
        const formGroup = service.createUserAdressFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserAdress should disable id FormControl', () => {
        const formGroup = service.createUserAdressFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
