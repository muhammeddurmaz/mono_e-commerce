import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductStatistics } from '../product-statistics.model';

@Component({
  selector: 'jhi-product-statistics-detail',
  templateUrl: './product-statistics-detail.component.html',
})
export class ProductStatisticsDetailComponent implements OnInit {
  productStatistics: IProductStatistics | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productStatistics }) => {
      this.productStatistics = productStatistics;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
