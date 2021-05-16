(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Sous_familleDeleteController',Sous_familleDeleteController);

    Sous_familleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sous_famille'];

    function Sous_familleDeleteController($uibModalInstance, entity, Sous_famille) {
        var vm = this;

        vm.sous_famille = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sous_famille.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
