(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('document-article', {
            parent: 'entity',
            url: '/document-article',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.document_article.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-article/document-articles.html',
                    controller: 'Document_articleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('document_article');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('document-article-detail', {
            parent: 'document-article',
            url: '/document-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.document_article.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-article/document-article-detail.html',
                    controller: 'Document_articleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('document_article');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Document_article', function($stateParams, Document_article) {
                    return Document_article.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'document-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('document-article-detail.edit', {
            parent: 'document-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-article/document-article-dialog.html',
                    controller: 'Document_articleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Document_article', function(Document_article) {
                            return Document_article.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('document-article.new', {
            parent: 'document-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-article/document-article-dialog.html',
                    controller: 'Document_articleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                path: null,
                                pathContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('document-article', null, { reload: 'document-article' });
                }, function() {
                    $state.go('document-article');
                });
            }]
        })
        .state('document-article.edit', {
            parent: 'document-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-article/document-article-dialog.html',
                    controller: 'Document_articleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Document_article', function(Document_article) {
                            return Document_article.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document-article', null, { reload: 'document-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('document-article.delete', {
            parent: 'document-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-article/document-article-delete-dialog.html',
                    controller: 'Document_articleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Document_article', function(Document_article) {
                            return Document_article.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document-article', null, { reload: 'document-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
