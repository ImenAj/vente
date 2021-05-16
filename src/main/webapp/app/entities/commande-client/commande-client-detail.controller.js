(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('CommandeClientDetailController', CommandeClientDetailController);

    CommandeClientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CommandeClient'];

    function CommandeClientDetailController($scope, $rootScope, $stateParams, previousState, entity, CommandeClient) {
        var vm = this;

        vm.commandeClient = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:commandeClientUpdate', function(event, result) {
            vm.commandeClient = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
