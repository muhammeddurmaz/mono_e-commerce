import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PropertyDesDetailComponent } from './property-des-detail.component';

describe('PropertyDes Management Detail Component', () => {
  let comp: PropertyDesDetailComponent;
  let fixture: ComponentFixture<PropertyDesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PropertyDesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ propertyDes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PropertyDesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PropertyDesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load propertyDes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.propertyDes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
