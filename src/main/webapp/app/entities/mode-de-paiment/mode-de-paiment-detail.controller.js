(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Mode_de_paimentDetailController', Mode_de_paimentDetailController);

    Mode_de_paimentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Mode_de_paiment'];

    function Mode_de_paimentDetailController($scope, $rootScope, $stateParams, previousState, entity, Mode_de_paiment) {
        var vm = this;

        vm.mode_de_paiment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:mode_de_paimentUpdate', function(event, result) {
            vm.mode_de_paiment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
