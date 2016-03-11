(function() {
  angular.module('pocAngularClient').controller('UnauthorizedController', UnauthorizedController);

  function UnauthorizedController() {
    var vm = this;
    vm.message = "";
  }
})();
