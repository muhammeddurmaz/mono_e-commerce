import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserCart } from '../user-cart.model';

@Component({
  selector: 'jhi-user-cart-detail',
  templateUrl: './user-cart-detail.component.html',
})
export class UserCartDetailComponent implements OnInit {
  userCart: IUserCart | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userCart }) => {
      this.userCart = userCart;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
