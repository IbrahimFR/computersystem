import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IComputer, NewComputer } from '../computer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IComputer for edit and NewComputerFormGroupInput for create.
 */
type ComputerFormGroupInput = IComputer | PartialWithRequiredKeyOf<NewComputer>;

type ComputerFormDefaults = Pick<NewComputer, 'id'>;

type ComputerFormGroupContent = {
  id: FormControl<IComputer['id'] | NewComputer['id']>;
  macAddress: FormControl<IComputer['macAddress']>;
  ipAddress: FormControl<IComputer['ipAddress']>;
  employeeAbbreviation: FormControl<IComputer['employeeAbbreviation']>;
  description: FormControl<IComputer['description']>;
};

export type ComputerFormGroup = FormGroup<ComputerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ComputerFormService {
  createComputerFormGroup(computer: ComputerFormGroupInput = { id: null }): ComputerFormGroup {
    const computerRawValue = {
      ...this.getFormDefaults(),
      ...computer,
    };
    return new FormGroup<ComputerFormGroupContent>({
      id: new FormControl(
        { value: computerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      macAddress: new FormControl(computerRawValue.macAddress),
      ipAddress: new FormControl(computerRawValue.ipAddress),
      employeeAbbreviation: new FormControl(computerRawValue.employeeAbbreviation),
      description: new FormControl(computerRawValue.description),
    });
  }

  getComputer(form: ComputerFormGroup): IComputer | NewComputer {
    return form.getRawValue() as IComputer | NewComputer;
  }

  resetForm(form: ComputerFormGroup, computer: ComputerFormGroupInput): void {
    const computerRawValue = { ...this.getFormDefaults(), ...computer };
    form.reset(
      {
        ...computerRawValue,
        id: { value: computerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ComputerFormDefaults {
    return {
      id: null,
    };
  }
}
