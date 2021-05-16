(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_ReceptionDeleteController',Bons_de_ReceptionDeleteController);

    Bons_de_ReceptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bons_de_Reception'];

    function Bons_de_ReceptionDeleteController($uibModalInstance, entity, Bons_de_Reception) {
        var vm = this;

        vm.bons_de_Reception = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bons_de_Reception.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
