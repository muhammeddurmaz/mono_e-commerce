import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../color.test-samples';

import { ColorFormService } from './color-form.service';

describe('Color Form Service', () => {
  let service: ColorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ColorFormService);
  });

  describe('Service methods', () => {
    describe('createColorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createColorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
          })
        );
      });

      it('passing IColor should create a new form with FormGroup', () => {
        const formGroup = service.createColorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
          })
        );
      });
    });

    describe('getColor', () => {
      it('should return NewColor for default Color initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createColorFormGroup(sampleWithNewData);

        const color = service.getColor(formGroup) as any;

        expect(color).toMatchObject(sampleWithNewData);
      });

      it('should return NewColor for empty Color initial value', () => {
        const formGroup = service.createColorFormGroup();

        const color = service.getColor(formGroup) as any;

        expect(color).toMatchObject({});
      });

      it('should return IColor', () => {
        const formGroup = service.createColorFormGroup(sampleWithRequiredData);

        const color = service.getColor(formGroup) as any;

        expect(color).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IColor should not enable id FormControl', () => {
        const formGroup = service.createColorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewColor should disable id FormControl', () => {
        const formGroup = service.createColorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
