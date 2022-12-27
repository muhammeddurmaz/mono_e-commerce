import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../seller.test-samples';

import { SellerFormService } from './seller-form.service';

describe('Seller Form Service', () => {
  let service: SellerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SellerFormService);
  });

  describe('Service methods', () => {
    describe('createSellerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSellerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            lastName: expect.any(Object),
            shopName: expect.any(Object),
            mail: expect.any(Object),
            activated: expect.any(Object),
            image: expect.any(Object),
            tckn: expect.any(Object),
            phone: expect.any(Object),
            city: expect.any(Object),
            placedDate: expect.any(Object),
            user: expect.any(Object),
            sellerProductType: expect.any(Object),
          })
        );
      });

      it('passing ISeller should create a new form with FormGroup', () => {
        const formGroup = service.createSellerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            lastName: expect.any(Object),
            shopName: expect.any(Object),
            mail: expect.any(Object),
            activated: expect.any(Object),
            image: expect.any(Object),
            tckn: expect.any(Object),
            phone: expect.any(Object),
            city: expect.any(Object),
            placedDate: expect.any(Object),
            user: expect.any(Object),
            sellerProductType: expect.any(Object),
          })
        );
      });
    });

    describe('getSeller', () => {
      it('should return NewSeller for default Seller initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSellerFormGroup(sampleWithNewData);

        const seller = service.getSeller(formGroup) as any;

        expect(seller).toMatchObject(sampleWithNewData);
      });

      it('should return NewSeller for empty Seller initial value', () => {
        const formGroup = service.createSellerFormGroup();

        const seller = service.getSeller(formGroup) as any;

        expect(seller).toMatchObject({});
      });

      it('should return ISeller', () => {
        const formGroup = service.createSellerFormGroup(sampleWithRequiredData);

        const seller = service.getSeller(formGroup) as any;

        expect(seller).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISeller should not enable id FormControl', () => {
        const formGroup = service.createSellerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSeller should disable id FormControl', () => {
        const formGroup = service.createSellerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
