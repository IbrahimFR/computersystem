import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComputer } from '../computer.model';

@Component({
  selector: 'jhi-computer-detail',
  templateUrl: './computer-detail.component.html',
})
export class ComputerDetailComponent implements OnInit {
  computer: IComputer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ computer }) => {
      this.computer = computer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
