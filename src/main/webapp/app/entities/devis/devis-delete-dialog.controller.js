(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('DevisDeleteController',DevisDeleteController);

    DevisDeleteController.$inject = ['$uibModalInstance', 'entity', 'Devis'];

    function DevisDeleteController($uibModalInstance, entity, Devis) {
        var vm = this;

        vm.devis = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Devis.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
