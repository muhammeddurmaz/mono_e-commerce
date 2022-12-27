import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../property-des.test-samples';

import { PropertyDesFormService } from './property-des-form.service';

describe('PropertyDes Form Service', () => {
  let service: PropertyDesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PropertyDesFormService);
  });

  describe('Service methods', () => {
    describe('createPropertyDesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPropertyDesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detail: expect.any(Object),
            property: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });

      it('passing IPropertyDes should create a new form with FormGroup', () => {
        const formGroup = service.createPropertyDesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detail: expect.any(Object),
            property: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });
    });

    describe('getPropertyDes', () => {
      it('should return NewPropertyDes for default PropertyDes initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPropertyDesFormGroup(sampleWithNewData);

        const propertyDes = service.getPropertyDes(formGroup) as any;

        expect(propertyDes).toMatchObject(sampleWithNewData);
      });

      it('should return NewPropertyDes for empty PropertyDes initial value', () => {
        const formGroup = service.createPropertyDesFormGroup();

        const propertyDes = service.getPropertyDes(formGroup) as any;

        expect(propertyDes).toMatchObject({});
      });

      it('should return IPropertyDes', () => {
        const formGroup = service.createPropertyDesFormGroup(sampleWithRequiredData);

        const propertyDes = service.getPropertyDes(formGroup) as any;

        expect(propertyDes).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPropertyDes should not enable id FormControl', () => {
        const formGroup = service.createPropertyDesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPropertyDes should disable id FormControl', () => {
        const formGroup = service.createPropertyDesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
