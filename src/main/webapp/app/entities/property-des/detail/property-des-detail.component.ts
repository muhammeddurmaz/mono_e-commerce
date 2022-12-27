import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPropertyDes } from '../property-des.model';

@Component({
  selector: 'jhi-property-des-detail',
  templateUrl: './property-des-detail.component.html',
})
export class PropertyDesDetailComponent implements OnInit {
  propertyDes: IPropertyDes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyDes }) => {
      this.propertyDes = propertyDes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
