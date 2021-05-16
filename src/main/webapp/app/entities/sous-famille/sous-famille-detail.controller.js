(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Sous_familleDetailController', Sous_familleDetailController);

    Sous_familleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sous_famille', 'Famille'];

    function Sous_familleDetailController($scope, $rootScope, $stateParams, previousState, entity, Sous_famille, Famille) {
        var vm = this;

        vm.sous_famille = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:sous_familleUpdate', function(event, result) {
            vm.sous_famille = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
