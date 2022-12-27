import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PropertyDetailComponent } from './property-detail.component';

describe('Property Management Detail Component', () => {
  let comp: PropertyDetailComponent;
  let fixture: ComponentFixture<PropertyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PropertyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ property: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PropertyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PropertyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load property on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.property).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
