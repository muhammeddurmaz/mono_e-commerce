import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISellerStatistics } from '../seller-statistics.model';

@Component({
  selector: 'jhi-seller-statistics-detail',
  templateUrl: './seller-statistics-detail.component.html',
})
export class SellerStatisticsDetailComponent implements OnInit {
  sellerStatistics: ISellerStatistics | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sellerStatistics }) => {
      this.sellerStatistics = sellerStatistics;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
