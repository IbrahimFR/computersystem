import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ComputerFormService, ComputerFormGroup } from './computer-form.service';
import { IComputer } from '../computer.model';
import { ComputerService } from '../service/computer.service';

@Component({
  selector: 'jhi-computer-update',
  templateUrl: './computer-update.component.html',
})
export class ComputerUpdateComponent implements OnInit {
  isSaving = false;
  computer: IComputer | null = null;

  editForm: ComputerFormGroup = this.computerFormService.createComputerFormGroup();

  constructor(
    protected computerService: ComputerService,
    protected computerFormService: ComputerFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ computer }) => {
      this.computer = computer;
      if (computer) {
        this.updateForm(computer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const computer = this.computerFormService.getComputer(this.editForm);
    if (computer.id !== null) {
      this.subscribeToSaveResponse(this.computerService.update(computer));
    } else {
      this.subscribeToSaveResponse(this.computerService.create(computer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComputer>>): void {
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

  protected updateForm(computer: IComputer): void {
    this.computer = computer;
    this.computerFormService.resetForm(this.editForm, computer);
  }
}
