(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livraisonDeleteController',Bons_de_livraisonDeleteController);

    Bons_de_livraisonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bons_de_livraison'];

    function Bons_de_livraisonDeleteController($uibModalInstance, entity, Bons_de_livraison) {
        var vm = this;

        vm.bons_de_livraison = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bons_de_livraison.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
