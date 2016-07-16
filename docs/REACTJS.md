# Development with ReactJS & Liferay

## Application bootstrapping <a name="bootstrap"></a>

## Configuring path URL history <a name="pathhistory"></a>

## Configuring hash URL history <a name="hashhistory"></a>

For [ReactJS Router](https://github.com/reactjs/react-router) the trick is done by importing **hashHistory** instead of **browserHistory** from `react-router`:

```javascript
import { Router, Route, hashHistory } from 'react-router';

render((
    <Router history={hashHistory}>
        // ... Routes
    </Router>
), document.getElementById('reactRootElement'));
```
