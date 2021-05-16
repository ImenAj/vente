(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Document_articleDeleteController',Document_articleDeleteController);

    Document_articleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Document_article'];

    function Document_articleDeleteController($uibModalInstance, entity, Document_article) {
        var vm = this;

        vm.document_article = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Document_article.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
