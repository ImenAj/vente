(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Mode_de_paimentDeleteController',Mode_de_paimentDeleteController);

    Mode_de_paimentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Mode_de_paiment'];

    function Mode_de_paimentDeleteController($uibModalInstance, entity, Mode_de_paiment) {
        var vm = this;

        vm.mode_de_paiment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Mode_de_paiment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
