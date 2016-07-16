import {Component} from '@angular/core';

@Component({
  selector: 'Header',
  template: `
    <header class="header">
      <p class="header-title">
        <a href="https://github.com/FountainJS/generator-fountain-webapp" target="_blank">
          Fountain Generator
        </a>
      </p>
      <p class="header-date">
        Generated with FountainJS v0.5.4 on Tue Jul 12 2016 15:19:13 GMT+0300 (EEST)
      </p>
    </header>
  `
})
export class Header {}
