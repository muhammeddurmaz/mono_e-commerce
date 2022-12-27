import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ColorFormService } from './color-form.service';
import { ColorService } from '../service/color.service';
import { IColor } from '../color.model';

import { ColorUpdateComponent } from './color-update.component';

describe('Color Management Update Component', () => {
  let comp: ColorUpdateComponent;
  let fixture: ComponentFixture<ColorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let colorFormService: ColorFormService;
  let colorService: ColorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ColorUpdateComponent],
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
      .overrideTemplate(ColorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ColorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    colorFormService = TestBed.inject(ColorFormService);
    colorService = TestBed.inject(ColorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const color: IColor = { id: 456 };

      activatedRoute.data = of({ color });
      comp.ngOnInit();

      expect(comp.color).toEqual(color);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IColor>>();
      const color = { id: 123 };
      jest.spyOn(colorFormService, 'getColor').mockReturnValue(color);
      jest.spyOn(colorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ color });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: color }));
      saveSubject.complete();

      // THEN
      expect(colorFormService.getColor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(colorService.update).toHaveBeenCalledWith(expect.objectContaining(color));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IColor>>();
      const color = { id: 123 };
      jest.spyOn(colorFormService, 'getColor').mockReturnValue({ id: null });
      jest.spyOn(colorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ color: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: color }));
      saveSubject.complete();

      // THEN
      expect(colorFormService.getColor).toHaveBeenCalled();
      expect(colorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IColor>>();
      const color = { id: 123 };
      jest.spyOn(colorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ color });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(colorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
