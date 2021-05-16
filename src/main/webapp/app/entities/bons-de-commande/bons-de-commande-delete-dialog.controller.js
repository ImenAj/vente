(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_commandeDeleteController',Bons_de_commandeDeleteController);

    Bons_de_commandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bons_de_commande'];

    function Bons_de_commandeDeleteController($uibModalInstance, entity, Bons_de_commande) {
        var vm = this;

        vm.bons_de_commande = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bons_de_commande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
