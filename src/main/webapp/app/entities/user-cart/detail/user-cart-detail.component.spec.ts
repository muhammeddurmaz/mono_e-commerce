import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserCartDetailComponent } from './user-cart-detail.component';

describe('UserCart Management Detail Component', () => {
  let comp: UserCartDetailComponent;
  let fixture: ComponentFixture<UserCartDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserCartDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userCart: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserCartDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserCartDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userCart on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userCart).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
