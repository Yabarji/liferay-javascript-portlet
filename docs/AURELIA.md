# Development with Aurelia & Liferay

## Application bootstrapping <a name="bootstrap"></a>

#### Run application on designated root element

Add separate element, for example `div` and assign `id` and `aurelia-app` -attributes:

**index.html**

```html
<html>
  <head>
    <title>Aurelia App</title>
    <link rel="stylesheet" href="styles/styles.css">
    <link rel="shortcut icon" href="favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body> 
    <div id="main" aurelia-app="main"> <!-- Assign ID to root element -->
      <div class="splash">
        <div class="message">Aurelia App</div>
        <i class="fa fa-spinner fa-spin"></i>
      </div>
    </div>
    <script src="jspm_packages/system.js"></script>
    <script src="config.js"></script>
    <script>
      System.import('aurelia-bootstrapper');
    </script>
  </body>
</html>
```

Then find your Aurelia configuration file and use `setRoot`-function to start an application on the root element:

**bootstrap file**

```javascript
import 'bootstrap';

export function configure(aurelia) {
  aurelia.use
    .standardConfiguration()
    .developmentLogging();

  aurelia.start().then(a => a.setRoot('main', document.getElementById('main'))); // This line is key
  // First parameter is the value of aurelia-app -attribute, second is the id. Make sure id is unique.
}
```

After that follow either [Path URL history](#pathhistory) or [Hash URL history](#hashhistory) instructions.


## Configuring path URL history <a name="pathhistory"></a>

## Configuring hash URL history <a name="hashhistory"></a>

For [Aurelia](http://aurelia.io/) framework you need to change **pushState** to `false` when configuring routes. Example code below: 

```javascript
import {Router} from 'aurelia-router';

export class App {
	static inject() { return [Router]; }

	constructor(router) {
		this.router = router;
		this.router.configure(this.configureRoutes);
	}

	configureRoutes(cfg) {
		cfg.title = 'Aurelia App';
		cfg.options.pushState = false; // This setting here does the magic
		cfg.map([
			// your routes here
		]);
	}
}
```
