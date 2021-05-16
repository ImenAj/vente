(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('AvoirsDeleteController',AvoirsDeleteController);

    AvoirsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Avoirs'];

    function AvoirsDeleteController($uibModalInstance, entity, Avoirs) {
        var vm = this;

        vm.avoirs = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Avoirs.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
