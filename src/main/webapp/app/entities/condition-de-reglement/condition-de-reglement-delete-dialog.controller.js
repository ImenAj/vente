(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Condition_de_reglementDeleteController',Condition_de_reglementDeleteController);

    Condition_de_reglementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Condition_de_reglement'];

    function Condition_de_reglementDeleteController($uibModalInstance, entity, Condition_de_reglement) {
        var vm = this;

        vm.condition_de_reglement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Condition_de_reglement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
