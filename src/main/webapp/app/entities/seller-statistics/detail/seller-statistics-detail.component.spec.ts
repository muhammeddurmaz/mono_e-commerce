import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SellerStatisticsDetailComponent } from './seller-statistics-detail.component';

describe('SellerStatistics Management Detail Component', () => {
  let comp: SellerStatisticsDetailComponent;
  let fixture: ComponentFixture<SellerStatisticsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SellerStatisticsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sellerStatistics: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SellerStatisticsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SellerStatisticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sellerStatistics on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sellerStatistics).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
