(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Facture_achatDeleteController',Facture_achatDeleteController);

    Facture_achatDeleteController.$inject = ['$uibModalInstance', 'entity', 'Facture_achat'];

    function Facture_achatDeleteController($uibModalInstance, entity, Facture_achat) {
        var vm = this;

        vm.facture_achat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Facture_achat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
