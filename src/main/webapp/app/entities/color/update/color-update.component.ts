import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ColorFormService, ColorFormGroup } from './color-form.service';
import { IColor } from '../color.model';
import { ColorService } from '../service/color.service';

@Component({
  selector: 'jhi-color-update',
  templateUrl: './color-update.component.html',
})
export class ColorUpdateComponent implements OnInit {
  isSaving = false;
  color: IColor | null = null;

  editForm: ColorFormGroup = this.colorFormService.createColorFormGroup();

  constructor(
    protected colorService: ColorService,
    protected colorFormService: ColorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ color }) => {
      this.color = color;
      if (color) {
        this.updateForm(color);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const color = this.colorFormService.getColor(this.editForm);
    if (color.id !== null) {
      this.subscribeToSaveResponse(this.colorService.update(color));
    } else {
      this.subscribeToSaveResponse(this.colorService.create(color));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IColor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(color: IColor): void {
    this.color = color;
    this.colorFormService.resetForm(this.editForm, color);
  }
}
