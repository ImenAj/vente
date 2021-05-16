(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livrisonDeleteController',Bons_de_livrisonDeleteController);

    Bons_de_livrisonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bons_de_livrison'];

    function Bons_de_livrisonDeleteController($uibModalInstance, entity, Bons_de_livrison) {
        var vm = this;

        vm.bons_de_livrison = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bons_de_livrison.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
