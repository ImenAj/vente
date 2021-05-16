(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('AvoirsDetailController', AvoirsDetailController);

    AvoirsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Avoirs', 'Facture'];

    function AvoirsDetailController($scope, $rootScope, $stateParams, previousState, entity, Avoirs, Facture) {
        var vm = this;

        vm.avoirs = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:avoirsUpdate', function(event, result) {
            vm.avoirs = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
