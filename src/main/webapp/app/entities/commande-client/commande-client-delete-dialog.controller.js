(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('CommandeClientDeleteController',CommandeClientDeleteController);

    CommandeClientDeleteController.$inject = ['$uibModalInstance', 'entity', 'CommandeClient'];

    function CommandeClientDeleteController($uibModalInstance, entity, CommandeClient) {
        var vm = this;

        vm.commandeClient = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CommandeClient.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
