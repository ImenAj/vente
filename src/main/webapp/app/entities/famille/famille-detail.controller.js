(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FamilleDetailController', FamilleDetailController);

    FamilleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Famille'];

    function FamilleDetailController($scope, $rootScope, $stateParams, previousState, entity, Famille) {
        var vm = this;

        vm.famille = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:familleUpdate', function(event, result) {
            vm.famille = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
