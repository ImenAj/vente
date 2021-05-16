(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Document_articleDialogController', Document_articleDialogController);

    Document_articleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Document_article'];

    function Document_articleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Document_article) {
        var vm = this;

        vm.document_article = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.document_article.id !== null) {
                Document_article.update(vm.document_article, onSaveSuccess, onSaveError);
            } else {
                Document_article.save(vm.document_article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:document_articleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPath = function ($file, document_article) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        document_article.path = base64Data;
                        document_article.pathContentType = $file.type;
                    });
                });
            }
        };

    }
})();
